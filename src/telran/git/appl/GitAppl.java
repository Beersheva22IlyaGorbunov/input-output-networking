package telran.git.appl;

import telran.git.GitRepository;
import telran.git.GitRepositoryImpl;
import telran.git.appl.controller.GitApplControllerItems;
import telran.view.InputOutput;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class GitAppl {
	private static GitRepository gitRep;

	public static void main(String[] args) {
		gitRep = GitRepositoryImpl.init();
		InputOutput io = new StandardInputOutput();
		
		gitRep.addIgnoredFileNameExp("^[a-zA-Z0-9]+$");
		gitRep.addIgnoredFileNameExp("^\\.[a-zA-Z0-9]+$");
		gitRep.addIgnoredFileNameExp("^[a-zA-Z0-9]+\\.jar$");
		
		Menu mainMenu = GitApplControllerItems.getGitMenu(gitRep);
		mainMenu.perform(io);
	}

}
