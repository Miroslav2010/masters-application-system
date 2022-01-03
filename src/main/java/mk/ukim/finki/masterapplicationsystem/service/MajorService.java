package mk.ukim.finki.masterapplicationsystem.service;

import mk.ukim.finki.masterapplicationsystem.domain.Major;

import java.util.List;

public interface MajorService {

    List<Major> findAllMajors();

    Major findMajorById(String id);

    Major findMajorByName(String name);

    Major save(String name);

    Major update(String id, String name);

}
