package globalmm

import fastparse._
import ammonite.ops._
import fastparse.Parsed.Failure
import fastparse.Parsed.Success

object Main {
  def main(params: Array[String]): Unit = files.foreach { f =>
    val fileContent = read(f)
    parse(fileContent, Parser.prgm(_)) match {
      case fail: Failure => println(s"failed to parse ${f.relativeTo(dir)}: $fail")
      case _: Success[_] =>
    }
  }

  private val dir = pwd/"input"
  private val allFiles = ls.rec(dir).filter(_.isFile)
  private val validExt = Set("c", "cpp", "cxx", "tpp", "txx")
  private val ignore = Set("__MACOSX")
  private val files = allFiles.filter(f => validExt(f.ext.toLowerCase()) && !ignore.exists(f.toString().contains(_)))
}
