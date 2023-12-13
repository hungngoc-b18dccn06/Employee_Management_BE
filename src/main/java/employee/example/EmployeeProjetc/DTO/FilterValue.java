package employee.example.EmployeeProjetc.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class FilterValue {
    private Map<String, Object> filterValue;
    private String sort;
    private Integer pageSize;
    private Integer pageIndex;
}
