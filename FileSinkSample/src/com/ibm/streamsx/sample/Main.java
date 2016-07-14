package com.ibm.streamsx.sample;

import java.util.HashMap;
import java.util.Map;

import com.ibm.streams.operator.OutputTuple;
import com.ibm.streams.operator.StreamSchema;
import com.ibm.streams.operator.Type;
import com.ibm.streamsx.topology.TStream;
import com.ibm.streamsx.topology.Topology;
import com.ibm.streamsx.topology.context.StreamsContextFactory;
import com.ibm.streamsx.topology.function.BiFunction;
import com.ibm.streamsx.topology.spl.SPL;
import com.ibm.streamsx.topology.spl.SPLStream;
import com.ibm.streamsx.topology.spl.SPLStreams;

public class Main {

	static enum FileSinkFormat { csv };
	
	public static void main(String[] args) throws Exception {
		Topology t = new Topology("FileSink_Invoke_Sample");
		
		// Creating source stream
		TStream<String> src = t.strings("a", "b", "c", "d", "e");

		// Converting topology stream (TStream) to SPL stream (SPLStream)
		StreamSchema schema = Type.Factory.getStreamSchema("tuple<rstring data>");
		SPLStream splStream = convertToSPLStream(src, schema);
		
		// Invoking FileSink Operator
		Map<String, Object> params = new HashMap<>();
		params.put("file", "/tmp/test.txt");
		params.put("flush", SPL.createValue(1, com.ibm.streams.operator.Type.MetaType.UINT32));
		params.put("format", FileSinkFormat.csv);
		
		SPL.invokeSink("spl.adapter::FileSink", splStream, params);
						
		StreamsContextFactory.getStreamsContext("STANDALONE").submit(t).get();
	}

	private static SPLStream convertToSPLStream(TStream<String> tStream, StreamSchema schema) {
		return SPLStreams.convertStream(tStream, new BiFunction<String, OutputTuple, OutputTuple>() {
			private static final long serialVersionUID = 1L;

			@Override
			public OutputTuple apply(String data, OutputTuple outTuple) {
				outTuple.setString(0, data);
				return outTuple;
			}
			
		}, schema);
	}
}
