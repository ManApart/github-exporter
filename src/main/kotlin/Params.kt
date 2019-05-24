class Params(args: Array<String>) {
    val owner = getArg(args, 0)
    val repoId = getArg(args, 1)
    val repoName = getArg(args, 2)
    val token = getArg(args, 3)


    private fun getArg(args: Array<String>, n: Int) : String {
        if (args.size <= n) {
            throw IllegalArgumentException("Please provide all arguments: owner, repoId, repoName, token")
        }
        return args[n]
    }
}