package com.bootcamp.controller;

import com.bootcamp.model.Note;
import com.bootcamp.model.dto.NoteId;
import com.bootcamp.model.dto.NoteMessage;
import com.bootcamp.model.dto.NoteShareWith;
import com.bootcamp.model.dto.NoteUpdate;
import com.bootcamp.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;
    @GetMapping("/all")
    public List<Note> getAll(Principal principal){
        List<Note> notes = new java.util.ArrayList<>(List.of());
        notes.addAll(noteService.getPrivateNotes(principal.getName()));
        notes.addAll(noteService.getPublicNotes());
        notes.addAll(noteService.getSharedNotes(principal.getName()));
        return notes;
    }
    @GetMapping("/type")
    public List<Note> getNotes(Principal principal, @RequestParam String visibility) {
        return switch (visibility) {
            case "PRIVATE" -> noteService.getPrivateNotes(principal.getName());
            case "SHARED" -> noteService.getSharedNotes(principal.getName());
            case "PUBLIC" -> noteService.getPublicNotes();
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "visibility " + visibility + " is incorrect");
        };
    }

    @GetMapping("/{id}")
    public Note getNote(Principal principal, @PathVariable("id") Long id) {
        return noteService.getNote(id, principal.getName());
    }

    @PostMapping("/add")
    public HttpStatus addNote(Principal principal, @RequestBody NoteMessage noteMessage) {
        noteService.addNote(noteMessage.message(), principal.getName());
        return HttpStatus.CREATED;
    }

    @PostMapping("/edit")
    public HttpStatus updateNote(Principal principal, @RequestBody NoteUpdate noteUpdate) {
        noteService.updateNote(noteUpdate.id(), noteUpdate.message() , principal.getName());
        return HttpStatus.OK;
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteNote(Principal principal, @PathVariable Long id) {
        noteService.deleteNote(id, principal.getName());
        return HttpStatus.OK;
    }
    @PostMapping("/share")
    public HttpStatus shareWithUser(Principal principal,@RequestBody NoteShareWith noteShareWith) {
        noteService.shareWithUser(principal.getName(),noteShareWith.email(), noteShareWith.id());
        return HttpStatus.OK;
    }

    @PostMapping("/deShare")
    public HttpStatus deShareWithUser(Principal principal,@RequestBody NoteShareWith noteShareWith) {
        noteService.deShareWithUser(principal.getName(),noteShareWith.email(), noteShareWith.id());
        return HttpStatus.OK;
    }

    @PostMapping("/publish")
    public HttpStatus makeNotePublic(Principal principal,@RequestBody NoteId id){
        noteService.makeNotePublic(id.id(),principal.getName());
        return HttpStatus.OK;
    }

    @PostMapping("/upVoteNote")
    public Long upVoteNote(Principal principal,@RequestBody NoteId id) {
        return noteService.upVoteNote(principal.getName(), id.id());
    }

    @PostMapping("/downVoteNote")
    public Long downVoteNote(Principal principal,@RequestBody NoteId id) {
        return noteService.downVoteNote(principal.getName(), id.id());
    }

}
