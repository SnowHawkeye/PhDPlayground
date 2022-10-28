/*
Tutorial link: https://jena.apache.org/documentation/ontology/
Documentation link: https://jena.apache.org/documentation/javadoc/jena/org.apache.jena.core/module-summary.html
 */

@file:Suppress("UNREACHABLE_CODE")

package tutorials.jena_ontologies

import org.apache.jena.ontology.Individual
import org.apache.jena.ontology.OntModel
import org.apache.jena.ontology.OntModelSpec
import org.apache.jena.ontology.OntModelSpec.OWL_MEM
import org.apache.jena.ontology.OntModelSpec.OWL_MEM_MICRO_RULE_INF
import org.apache.jena.rdf.model.ModelFactory


fun main() {
    // createModel()
    listInferredTypes()
}

fun createModel() {
    /*
    Default ontology model settings are OWL-Full language, in-memory storage, and a simple RDFS inference.
    Inference may have an impact on performance and is not required in every application.
     */

    val defaultModel: OntModel = ModelFactory.createOntologyModel()
    val noInferenceModel: OntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM)
    // It is also possible to create a custom model specification,
    // either from scratch (with a constructor) or by modifying an existing one
    val spec = OntModelSpec(OntModelSpec.OWL_MEM)
        .apply { documentManager = TODO() } // use a custom document manager here
        .also { ModelFactory.createOntologyModel(it) }

}

fun listInferredTypes() {
    // create the base model
    val (namespace, base) = importExampleOntology()

    // create the reasoning model using the base
    val inf = ModelFactory.createOntologyModel(OWL_MEM_MICRO_RULE_INF, base)

    // create a dummy paper for this example
    val paper = base.getOntClass(namespace + "Paper")
    var p1: Individual = base.createIndividual(namespace + "paper1", paper)

    // list the asserted types
    p1.listRDFTypes(false).forEach {
        println(p1.uri + " is asserted in class " + it)
    }

    // list the inferred types
    p1 = inf.getIndividual(namespace + "paper1")
    p1.listRDFTypes(false).forEach {
        println(p1.uri + " is inferred to be in class " + it)
    }

}

private fun importExampleOntology(): Pair<String, OntModel> {
    val source = "src/main/kotlin/tutorials/jena_ontologies/eswc-ontology.rdf"
    val namespace = "http://www.eswc2006.org/technologies/ontology#"
    val model = ModelFactory.createOntologyModel(OWL_MEM)
    model.read(source, "RDF/XML") // read adds the statements
    return Pair(namespace, model)
}

fun ontologyClasses() {
    val source = "src/main/kotlin/tutorials/jena_ontologies/eswc-ontology.rdf"
    val namespace = "http://www.eswc2006.org/technologies/ontology#"
    val model = ModelFactory.createOntologyModel(OWL_MEM)
    model.read(source, "RDF/XML")

    val resource = model.getResource(namespace + "Paper")
    val paper = model.getOntClass(namespace + "Paper")
    //val paper = resource.`as`(OntClass::class.java) // second option

    val bestPaper = model.createClass(namespace + "BestPaper")
}

fun ontologyProperties() {
    val (namespace, model) = importExampleOntology()
    val programme = model.createClass(namespace + "Programme")
    val organizedEvent = model.createClass(namespace + "OrganizedEvent")

    val hasProgramme = model.createObjectProperty(namespace + "hasProgramme")
    hasProgramme.addDomain(organizedEvent)
    hasProgramme.addRange(programme)
    hasProgramme.addLabel("has programme", "en")
    model.createCardinalityQRestriction(
        /* uri = */ null,
        /* prop = */ hasProgramme,
        /* cardinality = */ 3,
        /* cls = */ programme
    )
}