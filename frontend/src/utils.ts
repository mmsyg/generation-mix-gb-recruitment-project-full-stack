// Color palette definitions
const FUEL_COLORS: Record<string, string> = {
  // Renewable sources (Greens/Blues)
  wind: "#00ff5a",
  solar: "#d4ff00",
  hydro: "#0092ff",
  biomass: "#66ff00",
  nuclear: "#9ad400",

  // Non-renewable sources (Greys/Blacks)
  gas: "#616161",
  coal: "#000000",
  imports: "#b0b0b0",
  other: "#e0e0e0",
  oil: "#2c2c2c",
};


// Helper function to determine the color based on fuel type.
export const getFuelColor = (name: string, isRenewable: boolean): string => {
  const normalizedName = name.toLowerCase();
  if (FUEL_COLORS[normalizedName]) {
    return FUEL_COLORS[normalizedName];
  }
  return isRenewable ? "#69f0ae" : "#616161";
};