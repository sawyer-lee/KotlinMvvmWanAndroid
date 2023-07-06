import com.android.build.gradle.AppExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ASMPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getExtensions().getByType(AppExtension.class).registerTransform(new ASMTransform());
    }
}
