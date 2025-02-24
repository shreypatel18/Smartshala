package SmartShala.SmartShala.Entities;

public class AnswerSheetDto{

    QuetionPaper quetionPaper;

    Answer answer;

    public QuetionPaper getQuetionPaper() {
        return quetionPaper;
    }
    public void setQuetionPaper(QuetionPaper quetionPaper) {
        this.quetionPaper = quetionPaper;
    }
    public Answer getAnswer() {
        return answer;
    }
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
