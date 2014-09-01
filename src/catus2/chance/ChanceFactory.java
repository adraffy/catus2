package catus2.chance;

import catus2.Unit;

@FunctionalInterface
public interface ChanceFactory<T extends AbstractChance> {

    T create(Unit owner);
    
    public final ChanceFactory<Probability> PROB = (o) -> new Probability(o);
    public final ChanceFactory<PPM> PPM = (o) -> new PPM(o);
    public final ChanceFactory<RPPM> RPPM = (o) -> new RPPM(o);
    
}
