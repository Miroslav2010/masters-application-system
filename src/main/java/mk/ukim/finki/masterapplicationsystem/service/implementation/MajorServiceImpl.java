package mk.ukim.finki.masterapplicationsystem.service.implementation;

import mk.ukim.finki.masterapplicationsystem.domain.Major;
import mk.ukim.finki.masterapplicationsystem.repository.MajorRepository;
import mk.ukim.finki.masterapplicationsystem.service.MajorService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {
    private final MajorRepository majorRepository;

    private final Logger logger = LoggerFactory.getLogger(MajorServiceImpl.class);
    public MajorServiceImpl(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    @Override
    public List<Major> findAllMajors() {
        return majorRepository.findAll();
    }

    @Override
    public Major findMajorById(String id) {
        return this.majorRepository.findById(id).orElseThrow(() -> new RuntimeException("Major with id: " + id + " not found"));
    }

    @Override
    public Major findMajorByName(String name) {
        return this.majorRepository.findByName(name).orElseThrow(() -> new RuntimeException("Major with name: " + name + " not found"));
    }

    @Override
    public Major save(String name) {
        Major major = new Major(name);
        major = majorRepository.save(major);
        logger.info(String.format("New major with name %s",name));
        return major;
    }

    @Override
    public Major update(String id, String name) {
        Major major = findMajorById(id);
        logger.info(String.format("Updated Major name to %s, old name was %s",name,major.getName()));
        major.setName(name);
        return major;
    }
}
