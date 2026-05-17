package dev.themajorones.models.constants;

public interface TaskLogConstant {

    public static interface Type {
        String CREATE_ANDROID_VM = "CREATE_ANDROID_VM";
    }

    public static interface Status {
        String PENDING = "PENDING";
        String QUEUED = "QUEUED";
        String RUNNING = "RUNNING";
        String SUCCESS = "SUCCESS";
        String FAILED = "FAILED";
        String CANCELLED = "CANCELLED";
    }
    
}
