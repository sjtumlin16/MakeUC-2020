module Simple(
  input  [1:0] io_in,
  output [1:0] io_out
);
  wire  _T = io_in == 2'h0; // @[orbital.scala 19:13]
  assign io_out = {{1'd0}, _T}; // @[orbital.scala 19:10]
endmodule
module Precinct(
  input  [1:0] io_0_ioin,
  output [1:0] io_0_ioout
);
  wire [1:0] mods_0_io_in; // @[orbital.scala 29:54]
  wire [1:0] mods_0_io_out; // @[orbital.scala 29:54]
  wire [1:0] mods_1_io_in; // @[orbital.scala 29:54]
  wire [1:0] mods_1_io_out; // @[orbital.scala 29:54]
  Simple mods_0 ( // @[orbital.scala 29:54]
    .io_in(mods_0_io_in),
    .io_out(mods_0_io_out)
  );
  Simple mods_1 ( // @[orbital.scala 29:54]
    .io_in(mods_1_io_in),
    .io_out(mods_1_io_out)
  );
  assign io_0_ioout = mods_0_io_out; // @[orbital.scala 83:19]
  assign mods_0_io_in = io_0_ioin; // @[orbital.scala 79:21]
  assign mods_1_io_in = io_0_ioin; // @[orbital.scala 80:21]
endmodule
module Tes(
  input        clock,
  input        reset,
  input  [1:0] io_in,
  output [1:0] io_out
);
  wire [1:0] inst_io_0_ioin; // @[orbital.scala 130:21]
  wire [1:0] inst_io_0_ioout; // @[orbital.scala 130:21]
  Precinct inst ( // @[orbital.scala 130:21]
    .io_0_ioin(inst_io_0_ioin),
    .io_0_ioout(inst_io_0_ioout)
  );
  assign io_out = inst_io_0_ioout; // @[orbital.scala 162:10]
  assign inst_io_0_ioin = io_in; // @[orbital.scala 161:20]
endmodule
