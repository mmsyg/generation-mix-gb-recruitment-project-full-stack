export interface EnergySource {
    name: string;
    percentage: number;
    renewable: boolean;
}

export interface EnergyReport {
    date: string;
    sources: EnergySource[];
    cleanEnergyPercent: number;
}

export interface OptimalWindowReport {
    start: string;
    end: string;
    avgCleanEnergyPercent: number;
}