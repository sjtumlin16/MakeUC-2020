`include "Tes.v"

module Tes_tb ();
	reg  clock;  
	reg  reset;
	reg  [1:0] io_in; 
	wire [1:0] io_out;

Tes dut(
	.clock (clock),
	.reset (reset),
	.io_in (io_in),
	.io_out(io_out)
);

initial begin
    $dumpfile("test.vcd");
    $dumpvars(0,io_out,io_in);
	io_in = 1;
	#10;
end

endmodule
