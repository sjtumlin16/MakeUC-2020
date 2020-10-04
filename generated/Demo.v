module Simple(
  input  [1:0] io_in,
  output [1:0] io_out
);
  assign io_out = io_in + 2'h1; // @[demo.scala 13:10]
endmodule
module BOE(
  input  [1:0] io_in1,
  input  [1:0] io_in2,
  output [1:0] io_out,
  output       io_valid
);
  assign io_out = io_in1; // @[orbital.scala 117:10]
  assign io_valid = io_in1 == io_in2; // @[orbital.scala 111:14 orbital.scala 114:14]
endmodule
module Precinct(
  input  [1:0] iosInner_0_ioin,
  output [1:0] iosInner_0_ioout,
  output       io_ready
);
  wire [1:0] mods_0_io_in; // @[orbital.scala 20:54]
  wire [1:0] mods_0_io_out; // @[orbital.scala 20:54]
  wire [1:0] mods_1_io_in; // @[orbital.scala 20:54]
  wire [1:0] mods_1_io_out; // @[orbital.scala 20:54]
  wire [1:0] boe_io_in1; // @[orbital.scala 68:19]
  wire [1:0] boe_io_in2; // @[orbital.scala 68:19]
  wire [1:0] boe_io_out; // @[orbital.scala 68:19]
  wire  boe_io_valid; // @[orbital.scala 68:19]
  Simple mods_0 ( // @[orbital.scala 20:54]
    .io_in(mods_0_io_in),
    .io_out(mods_0_io_out)
  );
  Simple mods_1 ( // @[orbital.scala 20:54]
    .io_in(mods_1_io_in),
    .io_out(mods_1_io_out)
  );
  BOE boe ( // @[orbital.scala 68:19]
    .io_in1(boe_io_in1),
    .io_in2(boe_io_in2),
    .io_out(boe_io_out),
    .io_valid(boe_io_valid)
  );
  assign iosInner_0_ioout = boe_io_out; // @[orbital.scala 84:19]
  assign io_ready = boe_io_valid; // @[orbital.scala 74:12]
  assign mods_0_io_in = iosInner_0_ioin; // @[orbital.scala 78:21]
  assign mods_1_io_in = iosInner_0_ioin; // @[orbital.scala 79:21]
  assign boe_io_in1 = mods_0_io_out; // @[orbital.scala 82:18]
  assign boe_io_in2 = mods_1_io_out; // @[orbital.scala 83:18]
endmodule
module Demo(
  input        clock,
  input        reset,
  input  [1:0] io_in,
  output [1:0] io_out,
  output       io_valid
);
  wire [1:0] inst_iosInner_0_ioin; // @[orbital.scala 92:21]
  wire [1:0] inst_iosInner_0_ioout; // @[orbital.scala 92:21]
  wire  inst_io_ready; // @[orbital.scala 92:21]
  Precinct inst ( // @[orbital.scala 92:21]
    .iosInner_0_ioin(inst_iosInner_0_ioin),
    .iosInner_0_ioout(inst_iosInner_0_ioout),
    .io_ready(inst_io_ready)
  );
  assign io_out = inst_iosInner_0_ioout; // @[demo.scala 26:10]
  assign io_valid = inst_io_ready; // @[demo.scala 27:12]
  assign inst_iosInner_0_ioin = io_in; // @[demo.scala 25:20]
endmodule
