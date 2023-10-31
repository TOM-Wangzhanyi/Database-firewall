package entity;

/**
 * ClassName: ModelResult
 * Package: entity
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/10/31 - 11:19
 * @Version: v1.0
 */
public class ModelResult {
    @Override
    public String toString() {
        return "ModelResult{" +
                "id=" + id +
                ", sqlContent='" + sqlContent + '\'' +
                ", predict='" + predict + '\'' +
                '}';
    }

    private Integer id ;
    private String sqlContent ;
    private String predict ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSqlContent() {
        return sqlContent;
    }

    public void setSqlContent(String sqlContent) {
        this.sqlContent = sqlContent;
    }

    public String getPredict() {
        return predict;
    }

    public void setPredict(String predict) {
        this.predict = predict;
    }

    public ModelResult(Integer id, String sqlContent, String predict) {
        this.id = id;
        this.sqlContent = sqlContent;
        this.predict = predict;
    }


}
