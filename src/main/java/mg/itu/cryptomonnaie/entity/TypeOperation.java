package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, doNotUseGetters = true)
@Entity
public class TypeOperation extends AbstractDesignatedEntity { }
