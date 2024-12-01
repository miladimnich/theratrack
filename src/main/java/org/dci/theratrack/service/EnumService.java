package org.dci.theratrack.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.dci.theratrack.enums.DifficultyLevel;
import org.dci.theratrack.enums.TreatmentStatus;
import org.springframework.stereotype.Service;

@Service
public class EnumService {


  // Method to get difficulty levels
  public List<String> getDifficultyLevels() {
    return Arrays.stream(DifficultyLevel.values())
        .map(Enum::name)
        .collect(Collectors.toList());
  }

  // Method to get treatment statuses
  public List<String> getTreatmentStatuses() {
    return Arrays.stream(TreatmentStatus.values())
        .map(Enum::name)
        .collect(Collectors.toList());
  }

}
