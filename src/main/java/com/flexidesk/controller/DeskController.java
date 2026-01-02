package com.flexidesk.controller;

import com.flexidesk.entity.Desk;
import com.flexidesk.repository.DeskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/desks")
public class DeskController {
    private final DeskRepository deskRepository;

    public DeskController(DeskRepository deskRepository) {
        this.deskRepository = deskRepository;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/desk")
    public ResponseEntity<Desk> createDesk(@RequestBody Desk desk){
        desk.setAvailable(desk.getTotalSlots());
        Desk savedDesk=deskRepository.save(desk);
        return new ResponseEntity<>(savedDesk, HttpStatus.CREATED);
    }
    @GetMapping("/allavailable")
    public ResponseEntity<List<Desk>> getAllAvailableDesk(){
        return ResponseEntity.ok(deskRepository.findAll());
    }
    @GetMapping("/available")
    public ResponseEntity<List<Desk>> getAvailableDesks() {
        return ResponseEntity.ok(deskRepository.findByAvailableGreaterThan(0));
    }

}
