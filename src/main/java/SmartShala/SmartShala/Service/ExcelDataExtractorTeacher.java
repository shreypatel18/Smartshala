package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.Entities.Student;
import SmartShala.SmartShala.Entities.Teacher;
import jakarta.persistence.Column;
import org.apache.commons.codec.language.bm.Rule;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelDataExtractorTeacher {

    public static List<Teacher> getListOfTeachers(MultipartFile multipartFile) throws IOException {

        List<Teacher> teacherList = new ArrayList<>();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(multipartFile.getInputStream());
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

        int rowIdx=0;
        Iterator<Row> rowIterator = xssfSheet.iterator();
        while (rowIterator.hasNext()){
            Row row = rowIterator.next();

            Teacher temp = new Teacher();
            int cellIdx =0;
            Iterator<Cell> cellIterator = row.iterator();

            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                switch (cellIdx){
                    case 0:
                        temp.setTeacherId((int) cell.getNumericCellValue());
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
                }
                cellIdx++;
            }
            teacherList.add(temp);
            rowIdx++;

        }
        return teacherList;
    }
}
