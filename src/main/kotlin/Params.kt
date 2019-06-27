class Params(args: Array<String>) {
    val mock = args.contains("mock")
    val owners = getArg(args, 0, "owner").split(",")
    val zenhubToken = getArg(args, 1, "none")
    val githubToken = getArg(args, 2, "none")


    private fun getArg(args: Array<String>, n: Int, default: String, required: Boolean = true) : String {
        return when {
            args.contains("mock") -> default
            n >= args.size && required -> throw IllegalArgumentException("Please provide all arguments: owner, zenhubToken, githubToken.")
            n>= args.size && !required -> default
            else -> args[n]
        }
    }
}