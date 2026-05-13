package dev.themajorones.models.constants;

public interface TaskLogConstant {

    public static interface Type {
        String TEST = "TEST";
    }

    public static interface Status {
        String PENDING = "Pending";
        String SUCCESS = "Success";
        String FAILED = "Failed";
    }
    
}
