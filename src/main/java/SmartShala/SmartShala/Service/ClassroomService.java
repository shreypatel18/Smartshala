package SmartShala.SmartShala.Service;


import SmartShala.SmartShala.CustomException.ClassRoomNotFoundException;
import SmartShala.SmartShala.CustomException.ClassroomAlreadyRegistered;
import SmartShala.SmartShala.Entities.Classroom;
import SmartShala.SmartShala.Repository.ClassRepo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ClassroomService {

    @Autowired
    ClassRepo classRepo;


    public Classroom getClassRoomById(int id) {
        return classRepo.findById(id).get();
    }

    public Classroom getClassRoomByName(String name) {

        if (!classRepo.findByName(name).isPresent())
            throw new ClassRoomNotFoundException("classroom " + name + " doesnt exists");

        return classRepo.findByName(name).get();
    }

    public Classroom addClassroom(Classroom classroom) {
        classroom.setClassId(0);
        if (classRepo.findByName(classroom.getName()).isPresent()) {
            throw new ClassroomAlreadyRegistered("classroom with this name already registered");
        }
        GoogleDriveService.createClassFolder(classroom.getName());
        return classRepo.save(classroom);
    }

    public Classroom updateClassroom(Classroom classroom) {
        return classRepo.save(classroom);
    }

    public List<Classroom> getAllClassrooms() {
        return classRepo.findAll();
    }

}
