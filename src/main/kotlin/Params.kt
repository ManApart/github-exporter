class Params(args: Array<String>) {
    val mock = args.contains("mock")
    val owner = getArg(args, mock,  0)
    val repoId = getArg(args, mock,1)
    val repoName = getArg(args, mock, 2)
    val token = getArg(args, mock,3)


    private fun getArg(args: Array<String>, mock: Boolean, n: Int) : String {
        return when {
            mock -> ""
            args.size <= n -> throw IllegalArgumentException("Please provide all arguments: owner, repoId, repoName, token")
            else -> args[n]
        }
    }
}