package com.bootcamp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@Table(name = "notes")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//  @Column(name = "user_name", nullable = false)
//  private String userName;

    @Column(name = "message", nullable = false)
    private String message;
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Transient
    private Long vote;

    @JsonIgnore
    private Set<String> upVotedBy;
    @JsonIgnore
    private Set<String> downVotedBy;


    public Long getVote() {
        vote =(long) upVotedBy.size()-downVotedBy.size();
        return vote + vote/9;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "shared_note_user",
            joinColumns = @JoinColumn( name = "note_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( name = "user_id", referencedColumnName = "id ")
    )
    private List<User> sharedWith;
}

