package com.bootcamp.repository;

import com.bootcamp.model.Note;
import com.bootcamp.model.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
//    @Query("FROM Note AS n WHERE n.user.id = :id")
//    List<Note> findNotesByUser(@Param("id") Long id);\
    List<Note> findNotesByVisibilityIs(Visibility visibility);

}
