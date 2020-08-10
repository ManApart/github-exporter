import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Assert.assertNotNull
import org.junit.Test
import kotlin.test.assertEquals

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

    // @Test
    // fun zenhubEpicIssues() {
    //     val epicIssues: EpicIssues = jacksonObjectMapper().readValue(ZENHUB_EPIC_ISSUES)
    //     assertNotNull(epicIssues)
    // }

    // @Test
    // fun zenhubEpic() {
    //     val epic: Epic = jacksonObjectMapper().readValue(ZENHUB_EPIC)
    //     assertNotNull(epic)
    // }

    @Test
    fun links() {
        val links = Links("<https://api.github.com/organizations/36278557/repos?page=2>; rel=\"next\", <https://api.github.com/organizations/36278557/repos?page=3>; rel=\"last\"")

        assertEquals(2, links.links.size)
        assertEquals("https://api.github.com/organizations/36278557/repos?page=2", links.links[0].url)
        assertEquals("https://api.github.com/organizations/36278557/repos?page=3", links.links[1].url)
        assertEquals("next", links.links[0].rel)
        assertEquals("last", links.links[1].rel)
    }


}