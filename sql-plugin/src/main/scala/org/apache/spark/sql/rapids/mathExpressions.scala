/*
 * Copyright (c) 2019, NVIDIA CORPORATION.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.rapids

import ai.rapids.cudf.{BinaryOp, DType, UnaryOp}
import ai.rapids.spark.{CudfBinaryExpression, CudfUnaryExpression}

import org.apache.spark.sql.catalyst.expressions.{Expression, ImplicitCastInputTypes}
import org.apache.spark.sql.types.{AbstractDataType, DataType, DecimalType, DoubleType, LongType, TypeCollection}

abstract class CudfUnaryMathExpression(name: String) extends CudfUnaryExpression
    with Serializable with ImplicitCastInputTypes {
  override def inputTypes: Seq[AbstractDataType] = Seq(DoubleType)
  override def dataType: DataType = DoubleType
  override def nullable: Boolean = true
  override def toString: String = s"$name($child)"
  override def prettyName: String = name
}

case class GpuAcos(child: Expression) extends CudfUnaryMathExpression("ACOS") {
  override def unaryOp: UnaryOp = UnaryOp.ARCCOS
  override def outputTypeOverride: DType = DType.FLOAT64
}

case class GpuAsin(child: Expression) extends CudfUnaryMathExpression("ASIN") {
  override def unaryOp: UnaryOp = UnaryOp.ARCSIN
  override def outputTypeOverride: DType = DType.FLOAT64
}

case class GpuAtan(child: Expression) extends CudfUnaryMathExpression("ATAN") {
  override def unaryOp: UnaryOp = UnaryOp.ARCTAN
  override def outputTypeOverride: DType = DType.FLOAT64
}

case class GpuCeil(child: Expression) extends CudfUnaryMathExpression("CEIL") {
  override def dataType: DataType = child.dataType match {
    case dt @ DecimalType.Fixed(_, 0) => dt
    case DecimalType.Fixed(precision, scale) =>
      DecimalType.bounded(precision - scale + 1, 0)
    case _ => LongType
  }

  override def inputTypes: Seq[AbstractDataType] =
    Seq(TypeCollection(DoubleType, DecimalType, LongType))

  override def unaryOp: UnaryOp = UnaryOp.CEIL
  override def outputTypeOverride: DType = DType.INT64
}

case class GpuCos(child: Expression) extends CudfUnaryMathExpression("COS") {
  override def unaryOp: UnaryOp = UnaryOp.COS
  override def outputTypeOverride: DType = DType.FLOAT64
}

case class GpuExp(child: Expression) extends CudfUnaryMathExpression("EXP") {
  override def unaryOp: UnaryOp = UnaryOp.EXP
  override def outputTypeOverride: DType = DType.FLOAT64
}

case class GpuFloor(child: Expression) extends CudfUnaryMathExpression("FLOOR") {
  override def dataType: DataType = child.dataType match {
    case dt @ DecimalType.Fixed(_, 0) => dt
    case DecimalType.Fixed(precision, scale) =>
      DecimalType.bounded(precision - scale + 1, 0)
    case _ => LongType
  }

  override def inputTypes: Seq[AbstractDataType] =
    Seq(TypeCollection(DoubleType, DecimalType, LongType))

  override def unaryOp: UnaryOp = UnaryOp.FLOOR
  override def outputTypeOverride: DType = DType.INT64
}

case class GpuLog(child: Expression) extends CudfUnaryMathExpression("LOG") {
  override def unaryOp: UnaryOp = UnaryOp.LOG
  override def outputTypeOverride: DType = DType.FLOAT64
}

case class GpuSin(child: Expression) extends CudfUnaryMathExpression("SIN") {
  override def unaryOp: UnaryOp = UnaryOp.SIN
  override def outputTypeOverride: DType = DType.FLOAT64
}

case class GpuSqrt(child: Expression) extends CudfUnaryMathExpression("SQRT") {
  override def unaryOp: UnaryOp = UnaryOp.SQRT
  override def outputTypeOverride: DType = DType.FLOAT64
}

case class GpuTan(child: Expression) extends CudfUnaryMathExpression("TAN") {
  override def unaryOp: UnaryOp = UnaryOp.TAN
  override def outputTypeOverride: DType = DType.FLOAT64
}

abstract class CudfBinaryMathExpression(name: String) extends CudfBinaryExpression
    with Serializable with ImplicitCastInputTypes {
  override def inputTypes: Seq[DataType] = Seq(DoubleType, DoubleType)
  override def toString: String = s"$name($left, $right)"
  override def prettyName: String = name
  override def dataType: DataType = DoubleType
}

case class GpuPow(left: Expression, right: Expression)
    extends CudfBinaryMathExpression("POWER") {
  override def binaryOp: BinaryOp = BinaryOp.POW
  override def outputTypeOverride: DType = DType.FLOAT64
}