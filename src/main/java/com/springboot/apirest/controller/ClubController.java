package com.springboot.apirest.controller;


import com.springboot.apirest.dao.Club;
import com.springboot.apirest.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubes")
public class ClubController {
    @Autowired
    private ClubService clubService;

    @GetMapping
    public List<Club> getAllClubes() {
        return clubService.getAllClubes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Club> getClubById(@PathVariable Integer id) {
        return clubService.getClubById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Club createClub(@RequestBody Club club) {
        return clubService.createClub(club);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Club> updateClub(@PathVariable Integer id, @RequestBody Club club) {
        return clubService.updateClub(id, club)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable Integer id) {
        if (clubService.deleteClub(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

