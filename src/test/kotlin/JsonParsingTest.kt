import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Assert.assertNotNull
import org.junit.Test

class JsonParsingTest {

    @Test
    fun githubRepo() {
        val repo: GithubRepo = jacksonObjectMapper().readValue(GITHUB_REPO)
        assertNotNull(repo)
    }

    @Test
    fun githubIssue() {
        val issue: GithubIssue = jacksonObjectMapper().readValue(GITHUB_ISSUE)
        assertNotNull(issue)
    }

    @Test
    fun githubIssues() {
        val issues: List<GithubIssue> = jacksonObjectMapper().readValue(GITHUB_REPO_ISSUES)
        assertNotNull(issues)
    }

    @Test
    fun zenhubEpicIssues() {
        val epicIssues: EpicIssues = jacksonObjectMapper().readValue(ZENHUB_EPIC_ISSUES)
        assertNotNull(epicIssues)
    }

    @Test
    fun zenhubEpic() {
        val epic: Epic = jacksonObjectMapper().readValue(ZENHUB_EPIC)
        assertNotNull(epic)
    }


}