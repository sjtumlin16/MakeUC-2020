// package orbital

// import chisel3._
// import chisel3.util._
// import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
// import scala.collection._

// class Simple() extends Module {
// 	val io = IO(new Bundle {
// 		val in = Input(UInt(2.W))
// 		val out = Output(UInt(2.W))
// 	})

// 	io.out := ~io.in
// }

// class Precinct(block: Simple) extends Module {
//   val io = IO(new Bundle {
//   	val in = Input(UInt(2.W))
//   	val out = Input(UInt(2.W))
//   })

//   val citizen1 = block
//   val citizen2 = block

//   citizen1.io.in := io.in
//   citizen2.io.in := io.in

//   val board = new BOE()

//   board.io.in1 := citizen1.io.out
//   board.io.in2 := citizen2.io.out

//   io.out := board.io.out
// }

// object withVoting {  // scalastyle:ignore object.name
//   /** Creates a new Clock and Reset scope
//     *
//     * @param clock the new implicit Clock
//     * @param reset the new implicit Reset
//     * @param block the block of code to run with new implicit Clock and Reset
//     * @return the result of the block
//     */
//   def apply[T <: Simple](block: T): Precinct = {
// 		val res = Module(new Precinct(block))

// 		res
// 	}
// }

// class BOE() extends Module {
//   val io = IO(new Bundle {
//   	val in1 = Input(UInt(2.W))
//     val in2 = Input(UInt(2.W))
//     val out = Output(UInt(2.W))
//   })

//   io.out := io.in1
// }

// class Tes() extends Module {
// 	val io = IO(new Bundle {
// 		val in = Input(UInt(2.W))
// 		val out = Output(UInt(2.W))
// 	})

// 	val inst = withVoting{Module(new Simple)}
// }

// object GenExamp extends App {
//   chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Tes())
// }

// /**
//   * Compute GCD using subtraction method.
//   * Subtracts the smaller from the larger until register y is zero.
//   * value in register x is then the GCD
//   */
// // class BOE(citizens : Module) extends Module {
// //   val io = IO(new Bundle {
// //   	val muxAry = (0 until ports).map(x => Module(new FooBar(args))).toList // store the list in muxAry
// //     val value1        = Input(UInt(16.W))
// //   })

// //   val citizen1 = citizens()

// //   withClockAndReset



// // muxAry(0).io.out := muxAry(1).io.in // use it however you want
// //   val x  = Reg(UInt())
// //   val y  = Reg(UInt())

// //   when(x > y) { x := x - y }
// //     .otherwise { y := y - x }

// //   when(io.loadingValues) {
// //     x := io.value1
// //     y := io.value2
// //   }

// //   io.outputGCD := x
// //   io.outputValid := y === 0.U
// // }
