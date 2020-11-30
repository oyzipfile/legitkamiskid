// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import java.util.Iterator;
import java.util.List;
import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.util.WebUtils;
import java.util.Arrays;
import me.xiam.creativehack.command.syntax.SyntaxChunk;
import me.xiam.creativehack.command.Command;

public class CreditsCommand extends Command
{
    public CreditsCommand() {
        super("credits", null, new String[0]);
        this.setDescription("Prints KAMI Blue's authors and contributors");
    }
    
    @Override
    public void call(final String[] args) {
        final List<Integer> exceptions = Arrays.asList(17222512, 27009727, 48992448, 19880089, 55198830, 24369412, 51212427, 11698651, 44139104, 59456376, 41800112, 52386117, 26636167, 22961592, 13212688, 50775247, 12820770, 11377481, 3837873, 49104462, 56689414, 58238984, 27856297);
        String message = "\nName (Github if not same as name)\n&l&9Author:\n086 (zeroeightysix)\n&l&9Contributors:\nBella (S-B99)\nhub (blockparole)\nSasha (EmotionalLove)\nQther (d1gress / Vonr)\ncats (Cuhnt)\nJack (jacksonellsworth03)\nTheBritishMidget (TBM)\nHamburger (Hamburger2k)\n0x2E (PretendingToCode)\nBattery Settings (Bluskript)\nAn-En (AnotherEntity)\nArisa (Arisa-Snowbell)\nJamie (jamie27)\nWaizy (WaizyNet)\nIt is the end (Itistheend)\nbabbaj\nCrystallinqq\nleijurv\nElementars";
        for (final WebUtils.GithubUser u : WebUtils.getContributors(exceptions)) {
            message = message.concat("\n" + u.login);
        }
        MessageSendHelper.sendChatMessage(message);
    }
}
