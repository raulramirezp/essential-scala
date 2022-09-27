package modellingdatawithtraits.calculator

sealed trait OperationResult

final case class Succeed(value: Int) extends OperationResult
final case class Fail(message: String) extends OperationResult