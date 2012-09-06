/**
 *    Copyright 2012 Nicholas Blair
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package nblair.pipeline;

import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Nicholas Blair
 */
@Qualifier("end")
public class TerminationStep<W> implements PipelineStep<W> {
	
	/*
	 * (non-Javadoc)
	 * @see com.github.nblair.pipeline.PipelineStep#getName()
	 */
	@Override
	public String getName() {
		return "end";
	}
	/*
	 * (non-Javadoc)
	 * @see com.github.nblair.pipeline.PipelineStep#accept(java.lang.Object)
	 */
	@Override
	public void accept(W workUnit) {
	}
	/*
	 * (non-Javadoc)
	 * @see com.github.nblair.pipeline.PipelineStep#blockUntilComplete()
	 */
	@Override
	public void blockUntilComplete() {
	}
	/*
	 * (non-Javadoc)
	 * @see com.github.nblair.pipeline.PipelineStep#getNextStep()
	 */
	@Override
	public PipelineStep<W> getNextStep() {
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see com.github.nblair.pipeline.PipelineStep#shouldProcess(java.lang.Object)
	 */
	@Override
	public boolean shouldProcess(W workUnit) {
		return true;
	}
}
