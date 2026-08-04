package model

class PreRequisito private constructor(val versao: String,
                                       val codigo: String,
                                       val codigoPreReq: String): Entity() {

    companion object {
        fun of(versao: String, codigo: String, codigoPreReq: String) =
            PreRequisito(versao, codigo, codigoPreReq)
    }
}