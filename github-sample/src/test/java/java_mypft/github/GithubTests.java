package java_mypft.github;

import com.google.common.collect.ImmutableMap;
import com.jcabi.github.*;
import org.testng.annotations.Test;

import java.io.IOException;

public class GithubTests {

  @Test
  public void testCommits() throws IOException {
    Github github = new RtGithub("61322bcf2b1da14c432a8921cf8989270de6e9a7");
    RepoCommits commits = github.repos().get(new Coordinates.Simple("MBarkovskaya", "java_mypft")).commits();
    for (RepoCommit commit : commits.iterate(new ImmutableMap.Builder<String, String >().build()))  {
      System.out.println(new RepoCommit.Smart(commit).message());
    }
  }
}
