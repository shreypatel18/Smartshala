package SmartShala.SmartShala.Service;



import SmartShala.SmartShala.Entities.Student;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Component
public class ExcelDataExtractorStudent {

    @Autowired
    ClassroomService classroomService;
    public List<Student> getListOfStudent(MultipartFile multipartFile) throws IOException {



        List<Student> studentList = new ArrayList<>();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(multipartFile.getInputStream());
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

        int rowIdx=0;
        Iterator<Row> rowIterator = xssfSheet.iterator();
        while (rowIterator.hasNext()){
            Row row = rowIterator.next();

            Student temp = new Student();
            int cellIdx =0;
            Iterator<Cell> cellIterator = row.iterator();

            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                switch (cellIdx){
                    case 0:
                        temp.setStudentId((int) cell.getNumericCellValue());
                        System.out.println(cell.getNumericCellValue());
                        break;
                    case 1:
                        temp.setName(cell.getStringCellValue());
                        System.out.println(cell.getStringCellValue());
                        break;
                    case 2:
                        temp.setAddress(cell.getStringCellValue());
                        System.out.println(cell.getStringCellValue());
                        break;

                    case 3:
                        temp.setGender(cell.getStringCellValue().charAt(0));
                        System.out.println(cell.getStringCellValue());
                        break;

                    case 4:
                        temp.setAge((int)cell.getNumericCellValue());
                        System.out.println(cell.getNumericCellValue());
                        break;

                    case 5:
                        temp.setEmail(cell.getStringCellValue());
                        System.out.println(cell.getStringCellValue());
                        break;


                    case 6:
                        temp.setClassroom(classroomService.getClassRoomByName("class"+(int)cell.getNumericCellValue()));
                        break;
                }

                cellIdx++;
            }
            studentList.add(temp);
            rowIdx++;

        }
        return studentList;
    }
}

