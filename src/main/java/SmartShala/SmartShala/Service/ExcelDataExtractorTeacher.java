package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.CustomException.InvalidExcelDataException;
import SmartShala.SmartShala.Entities.Teacher;
import SmartShala.SmartShala.Repository.TeacherRepository;
import org.apache.poi.ss.usermodel.*;
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
public class ExcelDataExtractorTeacher {


    public static List<Teacher> getListOfTeachers(MultipartFile multipartFile) throws IOException {
        List<Teacher> teacherList = new ArrayList<>();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(multipartFile.getInputStream());

        // Validate if workbook has sheets
        if (xssfWorkbook.getNumberOfSheets() == 0) {
            throw new InvalidExcelDataException("uploaded workbook have no sheets");
        }

        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);


        int rowIdx = 0;
        System.out.println("inside the excel data extractor");

        Iterator<Row> rowIterator = xssfSheet.iterator();


        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            System.out.println("inside row loop");


            Teacher temp = new Teacher();
            int cellIdx = 0;
            Iterator<Cell> cellIterator = row.iterator();
            int cellCount = 0;
            System.out.println("in data extractor");

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                System.out.println("in while loop");
                switch (cellIdx) {
                    case 0: // Teacher ID
                        if (cell.getCellType() != CellType.NUMERIC) {
                            throw new InvalidExcelDataException("Teacher ID must be a numeric value at row " + rowIdx);
                        }
                        if (cell.getNumericCellValue() < 10000 || cell.getNumericCellValue() > 99999) {
                            throw new InvalidExcelDataException("ID must be exactly 6 digits");
                        }
                        System.out.println("teacherid is " + cell.getNumericCellValue());
                        temp.setTeacherId((int) cell.getNumericCellValue());
                        break;

                    case 1: // Name
                        if (cell.getCellType() != CellType.STRING) {
                            throw new InvalidExcelDataException("Teacher name must be a text value at row " + rowIdx);
                        }
                        temp.setName(cell.getStringCellValue().trim());
                        break;

                    case 2: // Address
                        if (cell.getCellType() != CellType.STRING) {
                            throw new InvalidExcelDataException("Address must be a text value at row " + rowIdx);
                        }
                        temp.setAddress(cell.getStringCellValue().trim());
                        break;

                    case 3: // Gender
                        if (cell.getCellType() != CellType.STRING || cell.getStringCellValue().length() != 1) {
                            throw new InvalidExcelDataException("Gender must be a single character (M/F) at row " + rowIdx);
                        }
                        temp.setGender(cell.getStringCellValue().toUpperCase().charAt(0));
                        break;

                    case 4: // Age
                        if (cell.getCellType() != CellType.NUMERIC) {
                            throw new InvalidExcelDataException("Age must be a numeric value at row " + rowIdx);
                        }
                        int age = (int) cell.getNumericCellValue();
                        if (age < 18 || age > 100) {
                            throw new InvalidExcelDataException("Age must be between 18 and 100 at row " + rowIdx);
                        }
                        temp.setAge(age);
                        break;

                    case 5: // Email
                        if (cell.getCellType() != CellType.STRING) {
                            throw new InvalidExcelDataException("Email must be a text value at row " + rowIdx);
                        }
                        String email = cell.getStringCellValue().trim();
                        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                            throw new InvalidExcelDataException("Invalid email format at row " + rowIdx);
                        }
                        temp.setEmail(email);
                        break;

                    default:
                        break;
                }
                cellIdx++;
                cellCount++;
            }
            if (cellCount != 6)
                throw new InvalidExcelDataException("there must be 6 fields in each row the sheet has " + cellCount + "at row " + rowIdx);
            teacherList.add(temp);
            rowIdx++;
        }
        return teacherList;
    }
}
