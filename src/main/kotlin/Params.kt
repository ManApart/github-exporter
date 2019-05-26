class Params(args: Array<String>) {
    val mock = args.contains("mock")
    val owner = getArg(args, 0, "owner")
    val token = getArg(args, 1, "none")


    private fun getArg(args: Array<String>, n: Int, default: String) : String {
        return when {
            args.contains("mock") -> default
            n >= args.size -> throw IllegalArgumentException("Please provide both arguments: owner, token.")
            else -> args[n]
        }
    }
}