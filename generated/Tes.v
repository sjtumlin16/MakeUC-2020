module Simple(
  input  [1:0] io_in,
  output [1:0] io_out
);
  wire  _T = io_in == 2'h0; // @[orbital.scala 14:19]
  assign io_out = {{1'd0}, _T}; // @[orbital.scala 14:16]
endmodule
module BOE(
  input  [1:0] io_in1,
  output [1:0] io_out
);
  assign io_out = io_in1; // @[orbital.scala 60:10]
endmodule
module Precinct(
  input  [1:0] io_in,
  output [1:0] io_out
);
  wire [1:0] citizen1_io_in; // @[orbital.scala 69:37]
  wire [1:0] citizen1_io_out; // @[orbital.scala 69:37]
  wire [1:0] citizen2_io_in; // @[orbital.scala 69:37]
  wire [1:0] citizen2_io_out; // @[orbital.scala 69:37]
  wire [1:0] board_io_in1; // @[orbital.scala 29:21]
  wire [1:0] board_io_out; // @[orbital.scala 29:21]
  Simple citizen1 ( // @[orbital.scala 69:37]
    .io_in(citizen1_io_in),
    .io_out(citizen1_io_out)
  );
  Simple citizen2 ( // @[orbital.scala 69:37]
    .io_in(citizen2_io_in),
    .io_out(citizen2_io_out)
  );
  BOE board ( // @[orbital.scala 29:21]
    .io_in1(board_io_in1),
    .io_out(board_io_out)
  );
  assign io_out = board_io_out; // @[orbital.scala 34:10]
  assign citizen1_io_in = io_in; // @[orbital.scala 26:18]
  assign citizen2_io_in = io_in; // @[orbital.scala 27:18]
  assign board_io_in1 = citizen1_io_out; // @[orbital.scala 31:16]
endmodule
module Tes(
  input        clock,
  input        reset,
  input  [1:0] io_in,
  output [1:0] io_out
);
  wire [1:0] inst_io_in; // @[orbital.scala 47:33]
  wire [1:0] inst_io_out; // @[orbital.scala 47:33]
  Precinct inst ( // @[orbital.scala 47:33]
    .io_in(inst_io_in),
    .io_out(inst_io_out)
  );
  assign io_out = inst_io_out; // @[orbital.scala 72:10]
  assign inst_io_in = io_in; // @[orbital.scala 71:14]
endmodule
