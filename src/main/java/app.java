import util.FileUtils;

public class app {

    public static void main(String[] args) {
        FileUtils fu = new FileUtils();
        fu.generateFileAlphanumeric(5, 15);
        fu.sortFile();
    }
}
