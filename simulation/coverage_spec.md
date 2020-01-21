# Graql World Simulation Coverage

## Design patterns to cover

Here we have some classic design patterns that recur commonly in Graql schemas for all domains.

- [x] Group membership
- [x] Transitive hierarchy
- [x] Transitive group membership
- [x] Data-sourcing/prediction/location nested relations

## Patterns

Following are schema/data patterns that are cases that we would like to ensure work correctly and quickly. This may skip over obvious cases which have been assumed and already added to the World Simulation schema, in effect making this more of a running TODO.

### Abstract

- [x] Abstract type and non-abstract type hierarchies
### Keys 
- [x] Different types using the same attribute as their key (`identifier`)
- [x] Different types owning the same attribute, one as its key, the other not keyed (`name`)
- [x] Types using attributes as keys where the attribute types form a hierarchy (`identifier-double`)
- Keying with each datatype:
	- [x] long
	- [x] double
	- [x] string
	- [ ] boolean
	- [ ] date
- [x] Multiple keys for one thing type
- [ ] Multiple keys for one thing type, with different datatypes
- [x] Disallowed ~~An attribute owned by the parent but used as key by the child~~
- [ ] Keying on both of two attributes that are in a hierarchy together
- [ ] Can a subtype of a key be used to key the thing keyed by its super type (at insertion time)?

### Attributes

- [x] Attribute playing a (non-implicit) role in a relation
- [x] Attribute of attribute
- [ ] ~~Attribute of attribute of attribute~~
- [x] Attribute hierarchies
- All attribute datatypes:
	- [ ] long
	- [ ] double
	- [x] string
	- [ ] boolean
	- [ ] date
- Implicit relations:
  - [ ] adding new roleplayers to them
  - [x] adding attributes to them
- [x] Regex for strings
- [ ] Attribute value updates (deletion of implicit relations with via and adding new attribute)
- [ ] Box shape where two things are in a hierarchy each owning an attribute, where the attributes are also in a hierarchy

### Relations

Ternary and N-ary relations, with interesting numbers of roleplayers per role

- [x] One entity playing 3+ roles
- [ ] One relation playing 3+ roles
- [x] One relation with 3+ roles played
- [ ] Unary relation, where a thing instance plays one role once and there are no other roleplayers
- [x] Symmetric and antisymmetric relation
- [x] Transitive relation
- Reflexive relation (`data-sourcing` as an example relation, or a court ruling, which could be on another ruling). There are two senses here:
	- [ ] A relation that plays a role in itself
	- [ ] A thing instance that plays two roles in the same relation instance
- [x] The anti-transitive and anti-reflexive are defined easily by the lack of a transitive rule or a reflexive role
- [x] Relation/entity/attribute types playing two roles in a relation, where the instances only play one role each (`person` playing `husband` and `wife` in a marriage)
- [x] Ragged role hierarchy: a relation that inherits from a parent subtypes the parent's roles, but also introduces new roles not yet seen in the hierarchy
- [x] Inheriting from a role declared in the current relation (not the parent) (`part-time-employee` and `employee`)
- [ ] Inherit role from grandparent relation, skipping the parent (allowed?)

## Rule formulations
- [x] Inferred relation
- [ ] Inferred relation where the rule is written for the parent relation type
- [ ] Inferred role for an existing relation
- [x] Inferred attribute
- [x] Inferred attribute using value from the `when`, transposing to a different type
- [x] Inferred complex type
- [ ] Inferred entity (useless but why not)
- [x] Successive/compound/recursive rules
- [ ] Rules for relations that also have materialised instances
- [x] `when` bodies with a relation(s) where the relation(s) don't have a variable ascribed to them
- [ ] Utilising explanations of rules, also recursively

## World elements to model

- [ ] Trade between companies
- [ ] Governments and elections
- [ ] Legal system
- [ ] Taxation and finance
- [ ] Currency
- [ ] Scientific measurements
- [ ] Immigration/emmigration, residency
- [ ] Phone numbers for regex and country extensions

## Other concerns

- [ ] Data Deletion
- [ ] New schema additions or undefine statements
- [ ] (Future) see how quickly an indexed schema pattern comes into effect and the speed improvement
- Matching across data and schema in single queries, e.g.:
  - [ ] `match $d type my-attribute; $d datatype string; get;`