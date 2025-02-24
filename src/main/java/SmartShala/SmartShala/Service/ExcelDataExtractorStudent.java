package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.CustomException.InvalidExcelDataException;
import SmartShala.SmartShala.Entities.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;

@Component
public class ExcelDataExtractorStudent {

    @Autowired
    ClassroomService classroomService;

    public List<Student> getListOfStudent(MultipartFile multipartFile) throws IOException {
        List<Student> studentList = new ArrayList<>();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(multipartFile.getInputStream());

        // Check if the workbook has sheets
        if (xssfWorkbook.getNumberOfSheets() == 0) {
            throw new InvalidExcelDataException("uploaded workbook have no sheets");
        }

        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

        int rowIdx = 0;
        Iterator<Row> rowIterator = xssfSheet.iterator();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Student temp = new Student();

            int cellIdx = 0;

            Iterator<Cell> cellIterator = row.iterator();
            int cellCount = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                switch (cellIdx) {

                    case 0: // Student ID
                        if (cell.getCellType() == CellType.NUMERIC) {
                            int studentId = (int) cell.getNumericCellValue();
                            if (studentId < 100000 || studentId > 999999) {
                                throw new InvalidExcelDataException("ID must be exactly 6 digits at row " + rowIdx);
                            }
                            temp.setStudentId(studentId);
                        } else {
                            throw new InvalidExcelDataException("id must be numeric");
                        }
                        break;


                    case 1: // Name
                        if (cell.getCellType() != CellType.STRING) {
                            throw new InvalidExcelDataException("Student name must be a text value at row " + rowIdx);
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
                        if (age < 3 || age > 100) {
                            throw new InvalidExcelDataException("Age must be between 3 and 100 at row " + rowIdx);
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

                    case 6: // Classroom
                        if (cell.getCellType() != CellType.NUMERIC) {
                            throw new InvalidExcelDataException("Classroom  must be a numeric value at row " + rowIdx);
                        }
                        temp.setClassroom(classroomService.getClassRoomByName("class" + (int) cell.getNumericCellValue()));
                        break;

                    default:
                        break;
                }
                cellCount++;
                cellIdx++;
            }
            if (cellCount != 7)
                throw new InvalidExcelDataException("there must be 7 fields in each row the sheet has " + cellCount + "at row " + rowIdx);
            studentList.add(temp);
            rowIdx++;
        }
        return studentList;
    }
}
