package globalmm

import fastparse._, fastparse.JavaWhitespace._

object Parser {
  def CharNotIn[_: P](s: String*): P[Unit] = P(CharPred(c => s.forall(ss => !ss.contains(c))))
  def CharsWhileNotIn[_: P](s: String): P[Unit] = P(CharsWhile(c => !s.contains(c)))
  def CharsWhileNotIn[_: P](s: String, min: Int): P[Unit] = P(CharsWhile(c => !s.contains(c), min))
  def quoted[_: P](s: String): P[Unit] = P(s ~~ (CharsWhileNotIn(s ++ "\\\n") | "\\" ~~ "\"".?).rep ~~ s)
  def balanced[_: P](l: String, r: String): P[Unit] = P(l ~ (quoted("\"") | quoted("\'") | CharsWhileNotIn(l ++ r ++ "\"\' \n/") | "/").rep ~ (balanced(l, r) ~ (quoted("\"") | quoted("\'") | CharsWhileNotIn(l ++ r ++ "\"\' \n/") | "/").rep).rep ~ r)

  def prpr[_: P]: P[Unit] = P("#" ~~ CharsWhileIn(" \t", 0) ~~ StringInIgnoreCase("define", "undef", "include", "if", "ifdef", "ifndef", "else", "elif", "endif", "line", "error", "pragma") ~~ CharsWhileNotIn("\n", 0) ~~ ("\n" | End))
  def usng[_: P]: P[Unit] = P("using" ~ CharsWhileNotIn("(){};") ~ ";")
  def tydf[_: P]: P[Unit] = P("typedef" ~ CharsWhileNotIn("(){};") ~ ";")
  def tmpl[_: P]: P[Unit] = P("template" ~ balanced("<", ">"))
  def func[_: P]: P[Unit] = P(tmpl.? ~ ("operator" ~ "()" | CharsWhileNotIn("(){};")) ~ balanced("(", ")") ~ (":" ~ (CharsWhileNotIn("(){};") ~ (balanced("(", ")") | balanced("{", "}"))).rep(sep = ",") | "const".?) ~ (balanced("{", "}") | "=" ~ ("default" | "delete" | "0")) ~ ";".?)
  def cls[_: P]: P[Unit] = P(tmpl.? ~ ("class" | "struct") ~ CharsWhileNotIn("(){};") ~ balanced("{", "}").? ~ ";")
  def enm[_: P]: P[Unit] = P("enum" ~ ("class" | "struct").? ~ CharsWhileNotIn("(){};") ~ balanced("{", "}").? ~ ";")
  def prgm[_: P]: P[Unit] = P(Pass ~ (prpr | usng | tydf | func | cls | enm).rep ~ End)
}
