package com.bootcamp.service;

import com.bootcamp.model.Note;
import com.bootcamp.model.User;
import com.bootcamp.model.Visibility;
import com.bootcamp.repository.NoteRepository;
import com.bootcamp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    private boolean isPublic(Long id) {
        if(noteRepository.findById(id).isPresent())
            return noteRepository.findById(id).get().getVisibility()==Visibility.PUBLIC;
        return false;
    }
    private boolean isSharedWith(Long id, String email) {
        if(userRepository.findByEmail(email).isPresent()) {
            return userRepository.findByEmail(email).get().getSharedFrom().stream()
                    .anyMatch(note -> Objects.equals(id, note.getId()));
        }
        return false;
    }
    private boolean isOwner(Long id, String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return userRepository.findByEmail(email).get().getNotes().stream()
                    .anyMatch(note -> Objects.equals(id, note.getId()));
        }
        return false;
    }
    public Note getNote(Long id, String email) {
        Optional<Note> noteOpt = noteRepository.findById(id);
        if(noteOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"note with id"+id+" not found");
        if(isOwner(id,email) || isSharedWith(id,email) || isPublic(id))
            return noteOpt.get();
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong user");
    }


    public List<Note> getPrivateNotes(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return noteRepository.findNotesByVisibilityIs(Visibility.PRIVATE);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
    public List<Note> getSharedNotes(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user with email"+email+" not found");
        return userOpt.get().getSharedFrom();
    }

    public List<Note> getPublicNotes() {
        return  noteRepository.findNotesByVisibilityIs(Visibility.PUBLIC);
    }

    public void addNote(String message, String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "user with email"+email+" not found");
        // this builder is now the default constructor for this class, for initializing value edit here
        Note note = Note.builder()
                .message(message)
                .user(userOpt.get())
                .visibility(Visibility.PRIVATE)
                .upVotedBy(new HashSet<>())
                .downVotedBy(new HashSet<>())
                .build();
        noteRepository.save(note);
    }

    public void updateNote(Long id, String message, String email) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent() && user.get().getNotes().contains(note)) {
                note.setMessage(message);
                noteRepository.save(note);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteNote(Long id, String email) {
        if (isOwner(id, email)) {
            System.out.println(id + email);
            noteRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong user");
        }
    }

    public void shareWithUser(String ownerEmail,String email, Long id) {
        Optional<Note> noteOpt = noteRepository.findById(id);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(noteOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"note with id"+id+" not found");
        if(!isOwner(id,ownerEmail)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong user");
        if(userOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user with email"+email+" not found");
        if(isOwner(id,email)) return;
        Note note = noteOpt.get();
        User user = userOpt.get();
        note.setVisibility(Visibility.SHARED);
        user.getSharedFrom().add(note);
        note.getSharedWith().add(user);
        userRepository.save(user);
        noteRepository.save(note);
    }

    public void makeNotePublic(Long id, String email) {
        Optional<Note> noteOpt = noteRepository.findById(id);
        if(noteOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"note with id"+id+" not found");
        if(!isOwner(id,email)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong user");
        Note note = noteOpt.get();
        note.setVisibility(Visibility.PUBLIC);
        noteRepository.save(note);
    }

    public void deShareWithUser(String ownerEmail, String email, Long id) {
        Optional<Note> noteOpt = noteRepository.findById(id);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(noteOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"note with id"+id+" not found");
        if(!isOwner(id,ownerEmail)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong user");
        if(userOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user with email"+email+" not found");
        if(isOwner(id,email)) return;
        Note note = noteOpt.get();
        User user = userOpt.get();
        note.getSharedWith().removeIf(user1 -> user1.getEmail().equals(email));
        if(note.getSharedWith().isEmpty() && note.getVisibility()!=Visibility.PUBLIC)
            note.setVisibility(Visibility.PRIVATE);
        // Check if cascading works here
        noteRepository.save(note);

    }

    public Long upVoteNote(String email, Long id) {
        Optional<Note> noteOpt = noteRepository.findById(id);
        if(noteOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"note with id"+id+" not found");
        Note note = noteOpt.get();
        if(note.getVisibility()!=Visibility.PUBLIC)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "note not public");
        note.getDownVotedBy().removeIf(email1 -> email1.equals(email));
        note.getUpVotedBy().add(email);
        noteRepository.save(note);
        return note.getVote();
    }

    public Long downVoteNote(String email, Long id) {
        Optional<Note> noteOpt = noteRepository.findById(id);
        if(noteOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"note with id"+id+" not found");
        Note note = noteOpt.get();
        if(note.getVisibility()!=Visibility.PUBLIC)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "note not public");
        note.getUpVotedBy().removeIf(email1 -> email1.equals(email));
        note.getDownVotedBy().add(email);
        noteRepository.save(note);
        return note.getVote();
    }
}
