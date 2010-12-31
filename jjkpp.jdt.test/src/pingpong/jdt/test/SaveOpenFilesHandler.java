package pingpong.jdt.test;

import org.eclipse.core.resources.IProject;

@SuppressWarnings("restriction")
public class SaveOpenFilesHandler extends org.eclipse.debug.internal.ui.launchConfigurations.SaveScopeResourcesHandler
{   
       public void showSaveDialog(IProject project)
       {
           super.showSaveDialog(new IProject[] {project}, true, false);
           super.doSave();
       }
}
