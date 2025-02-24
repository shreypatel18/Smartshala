package SmartShala.SmartShala.Entities;

public class SubjectDto {

    int subCode;

    String subName;

    int teacherId;

    String teacherName;

    public int getSubCode() {
        return subCode;
    }
    public void setSubCode(int subCode) {
        this.subCode = subCode;
    }
    public String getSubName() {
        return subName;
    }
    public void setSubName(String subName) {
        this.subName = subName;
    }
    public int getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
    public String getTeacherName() {
        return teacherName;
    }
}
