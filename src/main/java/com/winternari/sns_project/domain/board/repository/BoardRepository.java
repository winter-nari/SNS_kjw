package com.winternari.sns_project.domain.board.repository;


import com.winternari.sns_project.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.image WHERE b.id = :id")
    Optional<Board> findBoardWithImage(@Param("id") Long id);

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.image")
    List<Board> findAllWithImage();

    Optional<Board> findByName(String name);

//    List<Board> findByTitleContaining(String keyword);
}
