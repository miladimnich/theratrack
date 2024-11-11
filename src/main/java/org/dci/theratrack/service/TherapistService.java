package org.dci.theratrack.service;

import org.dci.theratrack.entity.Therapist;
import org.dci.theratrack.repository.TherapistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TherapistService {
    @Autowired
    private TherapistRepository therapistRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Therapist createTherapist(Therapist therapist) {
        return therapistRepository.save(therapist);
    }

    public List<Therapist> getAllTherapists() {
        return therapistRepository.findAll();
    }

    public Optional<Therapist> getTherapistById(Long id) {
        return therapistRepository.findById(id);
    }

    public Therapist updateTherapist(Long id, Therapist updatedTherapist) {
        Therapist existingTherapist = therapistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Therapist not found"));
        modelMapper.typeMap(Therapist.class, Therapist.class).addMappings(mapper -> mapper.skip(Therapist::setId));
        modelMapper.map(updatedTherapist, existingTherapist);
        return therapistRepository.save(existingTherapist);
    }

    public void deleteTherapist(Long id) {
        therapistRepository.deleteById(id);
    }
}
