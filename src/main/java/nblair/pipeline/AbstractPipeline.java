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
/**
 * 
 */

package nblair.pipeline;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Abstract super type that implements the "pipeline" concept.
 * 
 * @author Nicholas Blair
 */
public abstract class AbstractPipeline<I, W> {

	protected final Log log = LogFactory.getLog(this.getClass());
	public static final String CONFIG_SYSTEM_PROPERTY = "edu.wisc.wisccal.classsched.pipeline.AbstractPipeline.CONFIG";
	public static final String CONFIG = System.getProperty(
			CONFIG_SYSTEM_PROPERTY,
			"pipeline.xml");

	private WorkUnitSource<I> workUnitSource;
	private PipelineStep<W> firstStep;
	/**
	 * @return the workUnitSource
	 */
	public WorkUnitSource<I> getWorkUnitSource() {
		return workUnitSource;
	}
	/**
	 * @param workUnitSource the workUnitSource to set
	 */
	public void setWorkUnitSource(WorkUnitSource<I> workUnitSource) {
		this.workUnitSource = workUnitSource;
	}
	/**
	 * @return the firstStep
	 */
	public PipelineStep<W> getFirstStep() {
		return firstStep;
	}
	/**
	 * @param firstStep the firstStep to set
	 */
	public void setFirstStep(PipelineStep<W> firstStep) {
		this.firstStep = firstStep;
	}

	/**
	 * Run the pipeline:
	 * For each W returned by the {@link WorkUnitSource}, invoke {@link #beforeEntry(Object)}, and if
	 * {@link #shouldPush(Object)} returns true, pass in to the {@link PipelineStep#accept(Object)} function
	 * for {@link #getFirstStep()}.
	 * Once the {@link WorkUnitSource} no longer returns any Ws, invoke {@link PipelineStep#blockUntilComplete()}
	 * on {@link #getFirstStep()}.
	 */
	public final void runPipeline() {
		try {
			WorkUnitCollection<I> collection = getWorkUnitSource().getWorkUnits();
			do {
				int acceptedCount = 0;
				for(Iterator<I> units = collection.iterator(); units.hasNext();) {
					I instance = units.next();
					W unit = constructWorkUnit(instance);
					beforeEntry(unit);
					if(shouldPush(unit)) {
						getFirstStep().accept(unit);
						acceptedCount++;
					}
				}
				afterCollectionIteration(acceptedCount);
			} while (!collection.isComplete());
		} finally {
			getFirstStep().blockUntilComplete();
		}
	}

	/**
	 * Loads a Spring {@link ApplicationContext} from the path specified in the
	 * {@link System} property: {@link #CONFIG_SYSTEM_PROPERTY}.
	 * Locates a {@link AbstractPipeline} bean, and invokes {@link #runPipeline()}.
	 * 
	 * @param args
	 */
	public static final void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(CONFIG);
		AbstractPipeline<?, ?> pipeline = applicationContext.getBean(AbstractPipeline.class);

		pipeline.runPipeline();
	}

	/**
	 * Construct a workUnit W that wraps instance I from the {@link WorkUnitSource}.
	 * 
	 * @param instance
	 * @return
	 */
	public abstract W constructWorkUnit(I instance);
	/**
	 * This method is called after retrieval from the {@link WorkUnitSource}, but before
	 * being pushed into the pipeline.
	 * 
	 * Implementations may mutate the workUnit.
	 * 
	 * @param workUnit
	 */
	public abstract void beforeEntry(W workUnit);

	/**
	 * @param workUnit
	 * @return true if this workunit should be pushed into the pipeline
	 */
	public abstract boolean shouldPush(W workUnit);

	/**
	 * This method is called after completion of 
	 * 
	 */
	public abstract void afterCollectionIteration(int accepted);
}
