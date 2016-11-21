package backEnd.instructions;

public enum SingleDataTransferType {
    LDR, LDRSB, LDREQ, LDRNE, LDRLT, STR, STRB

    /* x = number

      LDR rx, =x / =msg_x / [sp, #x] / [rx]

      LDRSB rx, [sp] / [rx] / [sp, #x]

      STR rx, [sp, #x] / [sp, #x]! / [sp] / [rx]

      STRB rx, [sp, #x] / [sp, #x]! / [sp] / [rx]
      */
}
