/*
Tutorial link: https://jena.apache.org/documentation/inference/
Documentation link: https://jena.apache.org/documentation/javadoc/jena/
*/
package tutorials.jena_inference

import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Property
import org.apache.jena.reasoner.ReasonerRegistry
import org.apache.jena.reasoner.rulesys.RDFSRuleReasonerFactory
import org.apache.jena.riot.RDFDataMgr
import org.apache.jena.vocabulary.RDFS
import org.apache.jena.vocabulary.ReasonerVocabulary


fun main() {
    exampleReasoner()
}

fun configureReasoner() {
    val reasoner = ReasonerRegistry.getOWLReasoner()

    val parameterToConfigure = null
    val valueOfParameter = null
    // the ReasonerVocabulary class contains the available parameters for built-in reasoners
    reasoner.setParameter(parameterToConfigure, valueOfParameter)

}

fun exampleReasoner() {
    val namespace = "urn:x-hp-jena:eg/"

    // Build a trivial example data set
    val rdfsExample = ModelFactory.createDefaultModel()
    val p: Property = rdfsExample.createProperty(namespace, "p")
    val q: Property = rdfsExample.createProperty(namespace, "q")
    rdfsExample.add(p, RDFS.subPropertyOf, q) // add statement: "p is a sub-property of q"
    // e.g. "q" could be "parentOf" and "p" could be "fatherOf"
    rdfsExample.createResource(namespace + "a").addProperty(p, "foo") // "foo" is the value of the property

    // Create an inference model from this example using RDFS inference
    val inferenceModel = ModelFactory.createRDFSModel(rdfsExample) // [*]
    inferenceModel.setDerivationLogging(true) // necessary to explain how inferences were made

    // and then check that "a" also has property "q" of value "foo"
    val a = inferenceModel.getResource(namespace + "a")
    val statement = a.getProperty(q)
    println("Statement: $statement")
    inferenceModel.getDerivation(statement).forEach { print("Derivation (statement explanation) :$it") }

    // A more customizable way to create the inference model
    val reasoner = ReasonerRegistry.getRDFSReasoner() // or even RDFSRuleReasonerFactory.theInstance().create(null)
    val infModelAlt = ModelFactory.createInfModel(reasoner, rdfsExample)

    // An example of custom reasoner (which displays less information when listing statements)
    val customReasoner = RDFSRuleReasonerFactory.theInstance().create(null)
    customReasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE)
}

fun dataValidation() {
    val data: Model = ModelFactory.createDefaultModel() // = RDFDataMgr.loadModel(modelName)
    val infModel = ModelFactory.createRDFSModel(data)
    val validity = infModel.validate()
    if (validity.isValid) {
        println("OK")
    } else {
        println("Conflicts")
        val i: Iterator<*> = validity.reports
        while (i.hasNext()) {
            println(" - " + i.next())
        }
    }
}
