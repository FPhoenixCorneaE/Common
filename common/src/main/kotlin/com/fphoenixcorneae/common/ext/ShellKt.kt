package com.fphoenixcorneae.common.ext

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

/**
 * result of command
 *  * [CmdResult.result]     means result of command, 0 means normal, else means error, same to execute in linux shell
 *  * [CmdResult.successMsg] means success message of command result
 *  * [CmdResult.errorMsg]   means error message of command result
 */
data class CmdResult(
    /** result of command */
    val result: Int,
    /** success message of command result */
    val successMsg: String? = null,
    /** error message of command result */
    val errorMsg: String? = null,
)

const val COMMAND_SU = "su"
const val COMMAND_SH = "sh"
const val COMMAND_EXIT = "exit\n"
const val COMMAND_LINE_END = "\n"

/**
 * check whether has root permission
 */
fun checkRootPermission(): Boolean =
    executeCmd(command = "echo root", isRoot = true, isNeedResultMsg = false).result == 0

/**
 * execute shell command, default return result msg
 * @param command         command
 * @param isRoot          whether need to run with root
 * @param isNeedResultMsg whether need result msg
 */
fun executeCmd(
    command: String?,
    isRoot: Boolean = true,
    isNeedResultMsg: Boolean = true
): CmdResult =
    executeCmd(arrayOf(command), isRoot, isNeedResultMsg)

/**
 * execute shell commands, default return result msg
 *
 * @param commands        command list
 * @param isRoot          whether need to run with root
 * @param isNeedResultMsg whether need result msg
 */
fun executeCmd(
    commands: List<String?>?,
    isRoot: Boolean = true,
    isNeedResultMsg: Boolean = true
): CmdResult =
    executeCmd(commands?.toTypedArray(), isRoot, isNeedResultMsg)

/**
 * execute shell commands
 *
 * @param commands        command array
 * @param isRoot          whether need to run with root
 * @param isNeedResultMsg whether need result msg
 *  * if [isNeedResultMsg] is false, [CmdResult.successMsg] is null and [CmdResult.errorMsg] is null.
 *  * if [CmdResult.result] is -1, there maybe some exception.
 */
fun executeCmd(
    commands: Array<String?>?,
    isRoot: Boolean = true,
    isNeedResultMsg: Boolean = true
): CmdResult {
    var result = -1
    if (commands == null || commands.isEmpty()) {
        return CmdResult(result, null, null)
    }
    var process: Process? = null
    var successResult: BufferedReader? = null
    var errorResult: BufferedReader? = null
    var successMsg: StringBuilder? = null
    var errorMsg: StringBuilder? = null
    var os: DataOutputStream? = null
    try {
        process = Runtime.getRuntime().exec(if (isRoot) COMMAND_SU else COMMAND_SH)
        os = DataOutputStream(process.outputStream)
        for (command in commands) {
            if (command == null) {
                continue
            }
            // do not use os.writeBytes(command), avoid chinese charset error
            os.write(command.toByteArray())
            os.writeBytes(COMMAND_LINE_END)
            os.flush()
        }
        os.writeBytes(COMMAND_EXIT)
        os.flush()
        result = process.waitFor()
        // get command result
        if (isNeedResultMsg) {
            successMsg = StringBuilder()
            errorMsg = StringBuilder()
            successResult = BufferedReader(InputStreamReader(process.inputStream))
            errorResult = BufferedReader(InputStreamReader(process.errorStream))
            var s: String?
            while (successResult.readLine().also { s = it } != null) {
                successMsg.append(s)
            }
            while (errorResult.readLine().also { s = it } != null) {
                errorMsg.append(s)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        closeIOQuietly(os, successResult, errorResult)
        process?.destroy()
    }
    return CmdResult(result, successMsg?.toString(), errorMsg?.toString())
}
