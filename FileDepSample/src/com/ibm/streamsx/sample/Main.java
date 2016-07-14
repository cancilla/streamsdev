package com.ibm.streamsx.sample;

import com.ibm.streamsx.topology.TStream;
import com.ibm.streamsx.topology.Topology;
import com.ibm.streamsx.topology.context.StreamsContext.Type;
import com.ibm.streamsx.topology.context.StreamsContextFactory;

public class Main {

	public static void main(String[] args) throws Exception {

		Topology t = new Topology("FileDepSample");
		t.addJarDependency("./words.jar");
		TStream<String> srcStream = t.source(new FileReader("words.txt"));
		srcStream.print();
		
		StreamsContextFactory.getStreamsContext(Type.STANDALONE).submit(t).get();
	}

}
