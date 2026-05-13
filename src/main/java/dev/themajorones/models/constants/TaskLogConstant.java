package dev.themajorones.models.constants;

public interface TaskLogConstant {

    public static interface Type {
        String TEST = "TEST";
    }

    public static interface Status {
        String PENDING = "pending";
        String SUCCESS = "success";
        String FAILED = "failed";
    }
    
}
